package com.psa.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.psa.entity.User;
import com.psa.entity.Vessel;
import com.psa.entity.user.MyUserDetails;
import com.psa.repository.UserRepository;
import com.psa.utility.PasswordUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VesselService vesselService;

    @Autowired
    private PasswordUtility passwordUtility;

    @Autowired
    private MailService mailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new MyUserDetails(user);
    }

    public List<User> listAll() {
        return userRepository.findAll();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User get(int id) {
        return userRepository.findById(id).get();
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }

    public Set<Integer> getSubscribedVesselId(String username) {
        return userRepository.findAllSubscriptionByUsername(username);
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    public void updateSubscriptions(String username, List<String> vesselIDs) {
        User user = getUserByUsername(username);
        Set<Vessel> subscription = new HashSet<>();
        for (String vesselId : vesselIDs) {
            subscription.add(vesselService.getOne(Integer.parseInt(vesselId)));
        }
        user.setSubscriptions(subscription);
        save(user);
    }

    public boolean checkIfUserExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public Set<Vessel> getSubscriptionsByUsername(String username) {
        return getUserByUsername(username).getSubscriptions();
    }

    public boolean changePassword(String username, String oldPassword, String newPassword, String cfmPassword) {
        if (!(newPassword.equals(cfmPassword))) {
            return false;
        }

        User user = userRepository.getUserByUsername(username);

        if (passwordUtility.verifyPassword(oldPassword, user.getPassword())) {
            user.setPassword(passwordUtility.encodePassword(newPassword));
            save(user);

            mailService.changePasswordEmail(user.getId());
            return true;
        }
        return false;
    }

    public boolean createNewUser(User user, String cfmPassword) {
        String password = user.getPassword();
        if ((cfmPassword == null && password != null) || (cfmPassword != null && password == null)) {
            return false;
        }

        if (cfmPassword != null && password != null) {
            if (!(user.getPassword().equals(cfmPassword)) || checkIfUserExists(user.getUsername())) {
                return false;
            }
        }

        if (password == null) {
            password = passwordUtility.generatePassayPassword();
        }
        user.setPassword(passwordUtility.encodePassword(password));

        if (user.getRole() == null) {
            user.setRole("USER");
        }
        user.setPreferences(User.getDefaultPreferences());
        save(user);
        mailService.createNewUserEmail(user, password);
        return true;
    }

    public boolean adminResetPassword(List<Integer> userIDs) {
        for (int userID : userIDs) {
            User user = get(userID);
            String password = passwordUtility.generatePassayPassword();
            user.setPassword(passwordUtility.encodePassword(password));
            save(user);
            mailService.adminResetPasswordEmail(user, password);
        }
        return true;
    }

    public boolean updateUsers(List<Integer> delete, List<Integer> reset, List<Integer> enabled) {
        if(delete == null) {
            delete = new ArrayList<>();
        }
        if(reset == null) {
            reset = new ArrayList<>();
        }
        if(enabled == null) {
            enabled = new ArrayList<>();
        }
        for (int userID : delete) {
            if (reset.contains(userID)) {
                reset.remove(reset.indexOf(userID));
            }
            if (enabled.contains(userID)) {
                enabled.remove(enabled.indexOf(userID));
            }
        }
        for (int userID : delete) {
            delete(userID);
        }

        List<User> users = listAll();
        for (User user : users) {
            if (user.isEnabled() && !(enabled.contains(user.getId()))) {
                user.setEnabled(false);
                save(user);
            } else if (!(user.isEnabled()) && enabled.contains(user.getId())) {
                user.setEnabled(true);
                save(user);
            }
        }

        for (int userID : reset) {
            User user = get(userID);
            String password = passwordUtility.generatePassayPassword();
            user.setPassword(passwordUtility.encodePassword(password));
            save(user);
            mailService.adminResetPasswordEmail(user, password);
        }
        return true;
    }

    public void updateUserPreferenceByUsername(List<String> preferences, String username) {
        User user = getUserByUsername(username);
        user.setPreferences(new HashSet<>(preferences));
        save(user);
    }
}
