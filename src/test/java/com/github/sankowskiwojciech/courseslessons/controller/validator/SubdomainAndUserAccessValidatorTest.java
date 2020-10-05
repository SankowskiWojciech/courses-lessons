package com.github.sankowskiwojciech.courseslessons.controller.validator;

import com.github.sankowskiwojciech.coursescorelib.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.SubdomainType;
import com.github.sankowskiwojciech.courseslessons.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.courseslessons.stub.SubdomainStub;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SubdomainAndUserAccessValidatorTest {
    private final SubdomainService subdomainServiceMock = mock(SubdomainService.class);
    private final SubdomainAndUserAccessValidator testee = new SubdomainAndUserAccessValidator(subdomainServiceMock);

    @Before
    public void reset() {
        Mockito.reset(subdomainServiceMock);
    }

    @Test
    public void shouldReturnNullWhenSubdomainNameIsEmpty() {
        //given
        String subdomainNameStub = StringUtils.EMPTY;
        String userEmailAddressStub = TUTOR_EMAIL_ADDRESS_STUB;

        //when
        Subdomain subdomain = testee.apply(subdomainNameStub, userEmailAddressStub);

        //then
        assertNull(subdomain);
    }

    @Test
    public void shouldReturnSubdomainCorrectly() {
        //given
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        String subdomainNameStub = subdomainStub.getAlias();
        String userEmailAddressStub = TUTOR_EMAIL_ADDRESS_STUB;

        when(subdomainServiceMock.readSubdomainInformationIfSubdomainExists(eq(subdomainNameStub))).thenReturn(subdomainStub);

        //when
        Subdomain subdomainResult = testee.apply(subdomainNameStub, userEmailAddressStub);

        //then
        verify(subdomainServiceMock).readSubdomainInformationIfSubdomainExists(eq(subdomainNameStub));
        verify(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(userEmailAddressStub));
        assertEquals(subdomainResult, subdomainStub);
    }
}