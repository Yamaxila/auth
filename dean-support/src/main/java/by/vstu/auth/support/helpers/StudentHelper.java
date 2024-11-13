package by.vstu.auth.support.helpers;

import by.vstu.auth.components.users.BaseUserHelper;
import by.vstu.auth.dto.UserDTO;
import by.vstu.auth.models.UserModel;
import by.vstu.auth.support.dto.StudentDTO;
import com.google.gson.Gson;

public class StudentHelper extends BaseUserHelper {

    public StudentHelper(String url, String token) {
        super(url, token);
    }

    @Override
    public String getName() {
        return "student";
    }


    @Override
    public UserDTO toUserDTO(UserModel userModel) {

        this.request.setBodyPathVariable(true);

        String res = this.getRequest().run(userModel.getExternalId().toString());
        StudentDTO response = new Gson().fromJson(res, StudentDTO.class);

        UserDTO dto = new UserDTO();

        dto.setRoles(userModel.getRolesAsString());
        dto.setScope(userModel.getScopes());
        dto.setUsername(userModel.getUsername());
        dto.setEmail(userModel.getEmail());

        dto.setSurname(response.getSurname());
        dto.setName(response.getName());
        dto.setPatronymic(response.getPatronymic());
        dto.setExternalId(response.getId());
        dto.setHelperName(this.getName());

        return dto;
    }
}
