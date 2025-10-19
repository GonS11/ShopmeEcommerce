package com.shopme.admin.setting.country;

import com.shopme.common.entity.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/countries")
public class CountryRestController
{
    @Autowired
    private CountryRepository repo;

    @GetMapping("/list")
    public List<Country> listAll()
    {
        return repo.findAllByOrderByNameAsc();
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Country country)
    {
        try
        {
            Country savedCountry = repo.save(country);
            return ResponseEntity.ok(Map.of(
                    "id", savedCountry.getId(),
                    "message", "Country saved successfully"
            ));
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to save country: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id)
    {
        if (!repo.existsById(id))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Country with ID " + id + " not found"));
        }

        try
        {
            repo.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Country deleted successfully"));
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete country: " + e.getMessage()));
        }
    }
}