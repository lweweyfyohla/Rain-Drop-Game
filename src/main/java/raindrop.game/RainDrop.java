package raindrop.game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.KeepOnScreenComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class RainDrop extends GameApplication {

    private Entity bucket;
    private double baseBucketSpeed = 15;
    private int missedAtZeroScore = 0;
    private boolean gameOver = false;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("RainDrop Game");
        settings.setVersion("1.0");
    }

    @Override
    protected void initGame() {
        FXGL.getWorldProperties().setValue("score", 0);
        FXGL.getWorldProperties().setValue("speedMultiplier", 1.0);

        bucket = FXGL.entityBuilder()
                .type(GameObjectType.BUCKET)
                .at(350, 550)
                .viewWithBBox(FXGL.texture("bucket.png", 73, 83))
                .with(new KeepOnScreenComponent())
                .with(new CollidableComponent(true))
                .buildAndAttach();

        // Spawn normal drops every second
        FXGL.run(() -> spawnDrop(), Duration.seconds(1));

        // Spawn golden drop every 10 seconds
        FXGL.run(() -> spawnGoldenDrop(), Duration.seconds(10));

        // Increase speed every 3 seconds
        FXGL.run(() -> {
            double currentMultiplier = FXGL.getd("speedMultiplier");
            FXGL.set("speedMultiplier", currentMultiplier + 0.05);
        }, Duration.seconds(3));

        FXGL.runOnce(() -> showGameOver(), Duration.seconds(30));

        FXGL.getEventBus().addEventHandler(DropMissedEvent.DROP_MISSED, event -> {
            onDropMissed();
        });
    }

    private void onDropMissed() {
        if (gameOver) return;

        int currentScore = FXGL.geti("score");

        if (currentScore > 0) {
            FXGL.inc("score", -1);
            missedAtZeroScore = 0;
        } else {
            missedAtZeroScore++;
            if (missedAtZeroScore >= 3) {
                showGameOver();
            }
        }
    }

    private void spawnDrop() {
        FXGL.entityBuilder()
                .type(GameObjectType.DROP)
                .at(FXGL.random(0, 780), 0)
                .viewWithBBox(FXGL.texture("raindrop.png", 23, 43))
                .with(new DropMovement())
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }

    private void spawnGoldenDrop() {
        if (gameOver) return;
        FXGL.entityBuilder()
                .type(GameObjectType.GOLDEN_DROP)
                .at(FXGL.random(0, 780), 0)
                .viewWithBBox(FXGL.texture("goldendrop.png", 23, 43))
                .with(new DropMovement())
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }

    private void showGameOver() {
        if (gameOver) return;
        gameOver = true;
        FXGL.getDialogService().showMessageBox("Game Over!\nYour score: " + FXGL.geti("score"), () -> {
            FXGL.getGameController().exit();
        });
    }

    private double getBucketSpeed() {
        double speedMultiplier = FXGL.getd("speedMultiplier");
        return baseBucketSpeed * speedMultiplier;
    }

    @Override
    protected void initInput() {
        FXGL.onKey(KeyCode.LEFT, () -> bucket.translateX(-getBucketSpeed()));
        FXGL.onKey(KeyCode.RIGHT, () -> bucket.translateX(getBucketSpeed()));
    }

    @Override
    protected void initPhysics() {
        // Normal drop collision — +1 point
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameObjectType.BUCKET, GameObjectType.DROP) {
            @Override
            protected void onCollisionBegin(Entity bucket, Entity drop) {
                drop.removeFromWorld();
                if (drop.getY() > 517) {
                    return;
                }
                FXGL.inc("score", 1);
                FXGL.play("waterdrip.wav");
            }
        });

        // Golden drop collision — +5 points
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameObjectType.BUCKET, GameObjectType.GOLDEN_DROP) {
            @Override
            protected void onCollisionBegin(Entity bucket, Entity goldenDrop) {
                goldenDrop.removeFromWorld();
                if (goldenDrop.getY() > 517) {
                    return;
                }
                FXGL.inc("score", 5);
                FXGL.play("waterdrip.wav");
            }
        });
    }

    @Override
    protected void initUI() {
        var scoreText = FXGL.getUIFactoryService().newText("Score: 0", Color.BLACK, 24);
        FXGL.addUINode(scoreText, 10, 20);

        FXGL.getWorldProperties().addListener("score", (old, newScore) -> {
            scoreText.setText("Score: " + newScore);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}