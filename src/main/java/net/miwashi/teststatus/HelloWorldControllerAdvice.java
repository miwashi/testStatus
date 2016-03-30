package net.miwashi.teststatus;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.Optional;

@ControllerAdvice
@RequestMapping(produces = "application/vnd.error")
@ResponseBody
public class HelloWorldControllerAdvice {

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler(FileNotFoundException.class)
    VndErrors fileNotFoundException(FileNotFoundException e){
        return this.error(e, e.getLocalizedMessage());
    }

    private <E extends Exception> VndErrors error(E e, String logref){
        String msg = Optional.of(e.getMessage()).orElse((e.getClass().getSimpleName()));
        return new VndErrors(logref, msg);
    }
}
