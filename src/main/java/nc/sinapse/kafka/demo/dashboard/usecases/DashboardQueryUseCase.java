package nc.sinapse.kafka.demo.dashboard.usecases;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface DashboardQueryUseCase {
    DashboardSnapshot chargerSnapshot();

    SseEmitter ouvrirFlux();
}
