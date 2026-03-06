package raindrop.game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.KeepOnScreenComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class RainDrop extends GameApplication {

    private Entity bucket;
    private int missedAtZeroScore = 0;
    private boolean gameOver = false;
    private double bucketA = 0;
    private final double MAX_SPEED = 30.0;
    private final double ACCEL = 0.35;
    private final double DECEL = 0.10;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("RainDrop Game");
        settings.setVersion("1.0");
    }

    @Override
    protected void initInput() {
        FXGL.getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                bucketA -= ACCEL;
                if (bucketA < -MAX_SPEED) bucketA = -MAX_SPEED;
            }

            @Override
            protected void onActionEnd() {
                if (bucketA < 0) bucketA = 0;
            }
        }, KeyCode.LEFT);

        FXGL.getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                bucketA += ACCEL;
                if (bucketA > MAX_SPEED) bucketA = MAX_SPEED;
            }

            @Override
            protected void onActionEnd() {
                if (bucketA > 0) bucketA = 0;
            }
        }, KeyCode.RIGHT);
    }

    @Override
    protected void onUpdate(double tpf) {
        if (Math.abs(bucketA) < DECEL) bucketA = 0;
        bucket.translateX(bucketA);
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

        FXGL.run(() -> spawnDrop(), Duration.seconds(1));
        FXGL.run(() -> spawnGoldenDrop(), Duration.seconds(10));
        FXGL.run(() -> {
            double currentMultiplier = FXGL.getd("speedMultiplier");
            FXGL.set("speedMultiplier", currentMultiplier + 0.05);
        }, Duration.seconds(3));

        FXGL.runOnce(() -> showGameOver(), Duration.seconds(30));

        FXGL.getEventBus().addEventHandler(DropMissedEvent.DROP_MISSED, event -> onDropMissed());
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

    private void triggerGoldenBurst(double spawnX) {
        for (int i = 0; i < 10; i++) {
            final int index = i;
            FXGL.runOnce(() -> {
                if (!gameOver) {
                    FXGL.entityBuilder()
                            .type(GameObjectType.DROP)
                            .at(spawnX, 0)
                            .viewWithBBox(FXGL.texture("raindrop.png", 23, 43))
                            .with(new DropMovement())
                            .with(new CollidableComponent(true))
                            .buildAndAttach();
                }
            }, Duration.seconds(index * 0.2));
        }
    }

    private void showGameOver() {
        if (gameOver) return;
        gameOver = true;
        FXGL.getDialogService().showMessageBox("Game Over!\nYour score: " + FXGL.geti("score"), () -> {
            FXGL.getGameController().exit();
        });
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameObjectType.BUCKET, GameObjectType.DROP) {
            @Override
            protected void onCollisionBegin(Entity bucket, Entity drop) {
                drop.removeFromWorld();
                FXGL.inc("score", 1);
                FXGL.play("waterdrip.wav");
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameObjectType.BUCKET, GameObjectType.GOLDEN_DROP) {
            @Override
            protected void onCollisionBegin(Entity bucket, Entity goldenDrop) {
                double spawnX = goldenDrop.getX();
                goldenDrop.removeFromWorld();
                if (goldenDrop.getY() > 517) {
                    return;
                }
                FXGL.inc("score", 5);
                FXGL.play("waterdrip.wav");
                triggerGoldenBurst(spawnX);
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