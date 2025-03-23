package ch.hftm.sport_ticker.services;

import java.util.List;

import ch.hftm.sport_ticker.model.EventType;
import ch.hftm.sport_ticker.model.GameMatch;
import ch.hftm.sport_ticker.model.MatchEvent;
import ch.hftm.sport_ticker.model.Player;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class MatchEventService {

    public List<MatchEvent> getEventsForMatch(GameMatch match) {
        return MatchEvent.findByMatch(match);
    }

    public MatchEvent findById(Long id) {
        return MatchEvent.findById(id);
    }

    @Inject
    @Channel("match-events")
    Emitter<String> eventEmitter;

    @Transactional
    public MatchEvent addEvent(GameMatch match, EventType type, Player player) {
        // Ensure match is managed to avoid "detached entity" error
        GameMatch managedMatch = GameMatch.findById(match.id);

        MatchEvent event = new MatchEvent();
        event.match = managedMatch;
        event.type = type;
        event.player = player;
        event.persist();

        if (type == EventType.GOAL && player != null && player.team != null) {
            if (player.team.equals(managedMatch.teamA))
                managedMatch.scoreA++;
            else if (player.team.equals(managedMatch.teamB))
                managedMatch.scoreB++;
            managedMatch.persist();
        }

        return event;
    }

    public void emitMatchEvent(MatchEvent event) {
        try {
            eventEmitter.send("Event: " + event.type + " by " + (event.player != null ? event.player.name : "unknown") +
                    " in match " + event.match.id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void deleteEvent(Long id) {
        MatchEvent event = findById(id);
        if (event != null) {
            event.delete();
        }
    }
}
