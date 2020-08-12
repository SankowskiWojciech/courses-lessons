package com.github.sankowskiwojciech.courseslessons.model.db.subdomainuseraccess;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SubdomainUserAccessEntityId implements Serializable {
    private String subdomainEmailAddress;
    private String userEmailAddress;
}
