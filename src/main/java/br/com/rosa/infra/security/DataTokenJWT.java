package br.com.rosa.infra.security;

import java.util.List;

public record DataTokenJWT(String token, List<String> role) {
}
