package by.gurinovich.ZapolZachetSpring.controllers.API;

import by.gurinovich.ZapolZachetSpring.DTO.GroupDTO;
import by.gurinovich.ZapolZachetSpring.services.GroupService;
import by.gurinovich.ZapolZachetSpring.utils.mappers.impl.GroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupAPIController {
    private final GroupService groupService;
    private final GroupMapper groupMapper;

    @GetMapping("")
    public ResponseEntity<List<GroupDTO>> getGroups(){
        return new ResponseEntity<>(groupMapper.toDTOs(groupService.getAll()), HttpStatusCode.valueOf(200));
    }


}
