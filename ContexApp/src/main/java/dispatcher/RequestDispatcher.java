package dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.Dto;
import dto.UserDto;
import model.User;
import protocol.Request;
import service.AuthService;

public class RequestDispatcher {
    private AuthService service;

    public RequestDispatcher(AuthService service){
        this.service = service;
    }

    public Dto doDispatch(Request req){
        ObjectMapper mapper = new ObjectMapper();
        UserDto u = null;
        if (req.getHeader().getTyp().equals("login")){
            User payload = mapper.convertValue(req.getPayload(), User.class);
            if (service.isRegistered(payload)){
            u = new UserDto(payload.getId(), payload.getLogin(), payload.getPassword(), payload.getRole());
            };
        }
        return u;
    }
}
