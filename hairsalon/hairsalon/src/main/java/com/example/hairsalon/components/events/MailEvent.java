package com.example.hairsalon.components.events;
import com.example.hairsalon.models.AccountEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class MailEvent extends ApplicationEvent {

    private AccountEntity user;


    private String Url;


    private String type;

    public MailEvent(Object source, AccountEntity user, String url, String type) {
        super(source);
        this.user = user;
        Url = url;
        this.type = type;
    }
}