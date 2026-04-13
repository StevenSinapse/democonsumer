package nc.sinapse.kafka.demo.dashboard.sse;

import lombok.extern.slf4j.Slf4j;
import nc.sinapse.kafka.demo.dashboard.usecases.DashboardStreamPort;
import nc.sinapse.kafka.demo.dashboard.usecases.DashboardUpdate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
public class DashboardSseBroadcaster implements DashboardStreamPort {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @Override
    public SseEmitter ouvrirFlux() {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(error -> emitters.remove(emitter));
        try {
            emitter.send(SseEmitter.event().name("connected").data("demo-consumer-live"));
        } catch (IOException e) {
            emitters.remove(emitter);
            emitter.completeWithError(e);
        }
        return emitter;
    }

    @Override
    public void diffuserMiseAJour(DashboardUpdate update) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("projection-update")
                        .id(update.entityId())
                        .data(update));
            } catch (IOException e) {
                log.warn("[SSE] client retire apres erreur: {}", e.getMessage());
                emitters.remove(emitter);
                emitter.complete();
            }
        }
    }
}
