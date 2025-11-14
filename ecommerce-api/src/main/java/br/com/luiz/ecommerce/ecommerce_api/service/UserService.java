package br.com.luiz.ecommerce.ecommerce_api.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.luiz.ecommerce.ecommerce_api.domain.Usuario;
import br.com.luiz.ecommerce.ecommerce_api.dtos.UserRequestDTO;
import br.com.luiz.ecommerce.ecommerce_api.dtos.UserResponseDTO;
import br.com.luiz.ecommerce.ecommerce_api.handler.EmailJaCadastradoException;
import br.com.luiz.ecommerce.ecommerce_api.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; 
    }

    public UserResponseDTO criarUsuario(UserRequestDTO userRequestDTO) {

        Optional<Usuario> usuarioExistente = userRepository.findByEmail(userRequestDTO.email());

        if(usuarioExistente.isPresent()){
            throw new EmailJaCadastradoException(userRequestDTO.email());
        }

        String senhaCriptografada =  passwordEncoder.encode(userRequestDTO.senha()); // Aqui você pode adicionar a lógica de criptografia da senha

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(userRequestDTO.nome());
        novoUsuario.setEmail(userRequestDTO.email());
        novoUsuario.setSenha(senhaCriptografada);

        Usuario usuarioSalvo = userRepository.save(novoUsuario);

        return new UserResponseDTO(
            usuarioSalvo.getId(),
            usuarioSalvo.getNome(),
            usuarioSalvo.getEmail(),
            usuarioSalvo.getDataCriacao(),
            null
        );
    }

    

}
