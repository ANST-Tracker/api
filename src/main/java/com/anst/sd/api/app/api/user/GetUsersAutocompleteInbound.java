package com.anst.sd.api.app.api.user;

import com.anst.sd.api.domain.user.User;

import java.util.List;

public interface GetUsersAutocompleteInbound {
    List<User> get(String nameFragment);
}
