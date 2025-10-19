package com.shopme.admin.setting.state;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import com.shopme.common.entity.StateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/states")
public class StateRestController
{
    @Autowired
    private StateRepository repo;

    @GetMapping("/list_by_country/{id}")
    public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId)
    {
        Country country = new Country(countryId);
        List<State> listStates = repo.findByCountryOrderByNameAsc(country);

        return listStates.stream()
                .map(state -> new StateDTO(state.getId(), state.getName()))
                .collect(Collectors.toList());
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody State state)
    {
        try
        {
            State savedState = repo.save(state);
            return ResponseEntity.ok(Map.of(
                    "id", savedState.getId(),
                    "message", "State saved successfully"
            ));
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to save state: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id)
    {
        if (!repo.existsById(id))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "State with ID " + id + " not found"));
        }

        try
        {
            repo.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "State deleted successfully"));
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete state: " + e.getMessage()));
        }
    }
}