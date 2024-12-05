package actors;

import akka.actor.AbstractActor;

public class NotificationActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    System.out.println("Notification: " + message);
                })
                .build();
    }
}