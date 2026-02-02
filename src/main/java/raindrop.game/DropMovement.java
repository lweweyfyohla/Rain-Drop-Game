package raindrop.game;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;

public class DropMovement extends Component {
    private double baseSpeed = 150;

    @Override
    public void onUpdate(double tpf) {
        double speedMultiplier = FXGL.getd("speedMultiplier");
        entity.translateY(baseSpeed * speedMultiplier * tpf);

        if (entity.getY() > 600) {
            FXGL.getEventBus().fireEvent(new DropMissedEvent());
            entity.removeFromWorld();
        }
    }
}
