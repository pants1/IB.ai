/* Copyright 2017-2021 Arraying
 *
 * This file is part of IB.ai.
 *
 * IB.ai is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * IB.ai is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with IB.ai. If not, see http://www.gnu.org/licenses/.
 */

package com.ibdiscord.ibai.integration.jpa;

import com.ibdiscord.ibai.entities.UserJoinOverride;
import com.ibdiscord.ibai.entities.UserRole;
import com.ibdiscord.ibai.repositories.UserJoinOverrideRepository;
import com.ibdiscord.ibai.repositories.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static com.ibdiscord.ibai.integration.jpa.JPAConstants.*;

/**
 * Represents a base test class that can be extended to test JPA functionality.
 * This will use an in-memory H2 instance to test any database connectivity
 * This is slightly overkill, but will ensure that JPA performs accordingly.
 */
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class JPATest {

    @Autowired
    private UserJoinOverrideRepository userJoinOverrideRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Test
    void overrideNonExistent() {
        assertFalse(userJoinOverrideRepository.findById(USER_1).isPresent());
    }

    @Test
    void overrideExistent() {
        userJoinOverrideRepository.save(new UserJoinOverride(USER_1, 1337));
        userJoinOverrideRepository.save(new UserJoinOverride(USER_2, 7331));
        Optional<UserJoinOverride> override = userJoinOverrideRepository.findById(USER_1);
        assertTrue(override.isPresent());
        assertEquals(1337, override.get().getOverride());
    }

    @Test
    void roleNonExistent() {
        assertEquals(0, userRoleRepository.findByUser(USER_1).size());
    }

    @Test
    void roleExistent() {
        userRoleRepository.save(new UserRole(USER_1, ROLE_1));
        userRoleRepository.save(new UserRole(USER_1, ROLE_2));
        userRoleRepository.save(new UserRole(USER_2, ROLE_1));
        List<UserRole> roles = Arrays.asList(
            new UserRole(USER_1, ROLE_1),
            new UserRole(USER_1, ROLE_2)
        );
        assertEquals(roles, userRoleRepository.findByUser(USER_1));
    }

}
