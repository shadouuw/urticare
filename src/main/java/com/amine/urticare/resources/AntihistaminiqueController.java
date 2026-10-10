package com.amine.urticare.resources;

import com.amine.urticare.resources.dto.AntihistaminiqueResponse;
import com.amine.urticare.service.IAntihistaminiqueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Public reference catalog (see ResourceServerConfig permitAll), not
// user-scoped — no @AuthenticationPrincipal needed.
@RestController
@RequestMapping("/api/antihistaminiques")
public class AntihistaminiqueController {

    private final IAntihistaminiqueService antihistaminiqueService;

    public AntihistaminiqueController(IAntihistaminiqueService antihistaminiqueService) {
        this.antihistaminiqueService = antihistaminiqueService;
    }

    @GetMapping
    public List<AntihistaminiqueResponse> getAll() {
        return antihistaminiqueService.getAll();
    }
}
