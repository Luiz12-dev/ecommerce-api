package br.com.luiz.ecommerce.ecommerce_api.handler;

public class EmailJaCadastradoException extends RuntimeException {
    
    public EmailJaCadastradoException(String email) {
        super("O email " + email + " já está cadastrado !.");
    }

}
