package nc.sinapse.kafka.demo.dashboard.controllers;

import lombok.RequiredArgsConstructor;
import nc.sinapse.kafka.demo.dashboard.usecases.DashboardQueryUseCase;
import nc.sinapse.kafka.demo.dashboard.usecases.DashboardSnapshot;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardQueryUseCase dashboardQueryUseCase;

    @GetMapping
    public DashboardSnapshot chargerSnapshot() {
        return dashboardQueryUseCase.chargerSnapshot();
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        return dashboardQueryUseCase.ouvrirFlux();
    }
}
