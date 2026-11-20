package name.saak.yealink.controller;

import org.springframework.web.bind.annotation.*;

import name.saak.yealink.TelephoneHandler;

@RestController
@RequestMapping("/api/calls")
public class CallController {

    private final TelephoneHandler telephoneHandler;

    public CallController(TelephoneHandler telephoneHandler) {
        this.telephoneHandler = telephoneHandler;
    }

    /**
     * Externe Systeme können hier Anrufe triggern
     * POST http://localhost:8080/api/calls
     * Body: {"phoneNumber": "+49301234567"}
     */
    @PostMapping
    public void receiveCall(@RequestParam String phoneNumber) {
        telephoneHandler.handleCall(phoneNumber);
    }

    @GetMapping("/health")
    public String health() {
        return "✓ Tel-Handler läuft";
    }
}
