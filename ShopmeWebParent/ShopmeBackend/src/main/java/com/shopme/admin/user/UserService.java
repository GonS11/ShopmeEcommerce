package com.shopme.admin.user;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService
{
    public static final int USERS_PER_PAGE = 4;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getByEmail(String email)
    {
        return userRepo.findByEmail(email);
    }

    public List<User> listAll()
    {
        return (List<User>) userRepo.findAll(Sort.by("firstName").ascending());
    }

    public void listByPage(int pageNum, PagingAndSortingHelper helper)
    {
        helper.listEntities(pageNum, USERS_PER_PAGE, userRepo);
    }

    public List<Role> listRoles()
    {
        return (List<Role>) roleRepo.findAll();
    }

    public User save(User user)
    {
        boolean isUpdatingUser = (user.getId() != null);

        if(isUpdatingUser)
        {
            User existingUser = userRepo.findById(user.getId())
                    .orElseThrow(() -> new UserNotFoundException("Could not find any user with ID " + user.getId()));

            if(user.getPassword().isEmpty())
            {
                user.setPassword(existingUser.getPassword());
            }
            else
            {
                encodePassword(user);
            }
        }
        else
        {
            encodePassword(user);
        }
        return userRepo.save(user);
    }

    public User updateAccount(User userInForm) {
        User userInDB = userRepo.findById(userInForm.getId())
                .orElseThrow(() -> new UserNotFoundException("Could not find any user with ID " + userInForm.getId()));

        if(!userInForm.getPassword().isEmpty()) {
            userInDB.setPassword(userInForm.getPassword());
            encodePassword(userInDB);
        }

        if(userInForm.getPhotos() != null) {
            userInDB.setPhotos(userInForm.getPhotos());
        }

        userInDB.setFirstName(userInForm.getFirstName());
        userInDB.setLastName(userInForm.getLastName());

        return userRepo.save(userInDB);
    }

    private void encodePassword(User user) {
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
    }

    public boolean isEmailUnique(Integer id, String email) {
        User userByEmail = userRepo.findByEmail(email);

        if(userByEmail == null) {
            return true;
        }

        if(id == null) {
            return false;
        }

        return userByEmail.getId().equals(id);
    }

    public User get(Integer id) throws UserNotFoundException
    {
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Could not find any user with ID " + id));
    }

    public void delete(Integer id) throws UserNotFoundException
    {
        Long countById = userRepo.countById(id);
        if (countById == null || countById == 0)
        {
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }

        userRepo.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id, boolean enabled)
    {
        userRepo.updateEnabledStatus(id, enabled);
    }
}
