package com.api.rest.service;

import com.api.rest.model.dto.UserDTO;
import lombok.NonNull;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface IKeycloakService {

    List<UserRepresentation> findAllUsers();
    List<UserRepresentation> searchUserByUsername(String username);

    String createUser(@NonNull UserDTO userDTO);

    void deleteUser(String userId);
    void updateUser(String userId, UserDTO userDTO);


}
