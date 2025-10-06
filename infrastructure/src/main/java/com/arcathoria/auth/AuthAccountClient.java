package com.arcathoria.auth;

interface AuthAccountClient {

    AccountView validate(final String email, final String password);
}
