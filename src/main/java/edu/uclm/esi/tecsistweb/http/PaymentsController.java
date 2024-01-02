package edu.uclm.esi.tecsistweb.http;

import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import edu.uclm.esi.tecsistweb.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;


@RestController
@RequestMapping("payments")
public class PaymentsController {
    @Value("${payments.publickey.stripe}")
    private String public_key;

    @Value("${payments.privatekey.stripe}")
    private String private_key;

    @Autowired
    private UserService userService;

    @GetMapping("/prepay/{matches}")
    public String prepay(HttpSession session, @PathVariable int matches){
        try {
            if (session.getAttribute("id_user")==null)
                throw new TySWebException(HttpStatus.FORBIDDEN,
                        new Exception("You must be logged in to buy matches"));

            Stripe.apiKey = private_key;

            long precio = matches * 100;
            PaymentIntentCreateParams params = new PaymentIntentCreateParams.Builder()
                    .setCurrency("eur")
                    .setAmount(precio)
                    .build();
            PaymentIntent intent = PaymentIntent.create(params);
            JSONObject jso = new JSONObject(intent.toJson());
            String clientSecret = jso.getString("client_secret");
            session.setAttribute("client_secret", clientSecret);
            session.setAttribute("matches", matches);
            return clientSecret;
        } catch (Exception e) {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception(e.getMessage()));
        }
    }

    @GetMapping("/confirm")
    public void confirm(HttpSession session){
        if (session.getAttribute("client_secret")==null
                || session.getAttribute("matches")==null
                || session.getAttribute("id_user")==null)
            throw new TySWebException(HttpStatus.FORBIDDEN, new Exception("Error trying to confirm the payment"));
        String userId = session.getAttribute("id_user").toString();
        this.userService.addMatches(userId, (Integer) session.getAttribute("matches"));
    }
}
