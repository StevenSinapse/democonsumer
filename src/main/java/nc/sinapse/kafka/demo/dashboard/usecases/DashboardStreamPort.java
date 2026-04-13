package nc.sinapse.kafka.demo.dashboard.usecases;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface DashboardStreamPort {
    SseEmitter ouvrirFlux();

    void diffuserMiseAJour(DashboardUpdate update);
}
