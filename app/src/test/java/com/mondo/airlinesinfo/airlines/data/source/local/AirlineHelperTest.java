package com.mondo.airlinesinfo.airlines.data.source.local;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.Sort;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by mahmoud on 11/15/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Realm.class, RealmQuery.class})
@SuppressStaticInitializationFor("io.realm.internal.Util")
public class AirlineHelperTest {

    private static final String AIRLINE_CODE_1 = "Code1";
    private static final String AIRLINE_CODE_2 = "Code2";

    private static final AirlineRealm AIRLINE1_REALM;
    private static final AirlineRealm AIRLINE2_REALM;

    static {
        AIRLINE1_REALM = new AirlineRealm();
        AIRLINE1_REALM.setCode(AIRLINE_CODE_1);

        AIRLINE2_REALM = new AirlineRealm();
        AIRLINE2_REALM.setCode(AIRLINE_CODE_2);
    }

    private Realm mRealm;
    private RealmQuery<AirlineRealm> mQuery;

    @Before
    public void setUp() {
        mockStatic(Realm.class);

        mRealm = mock(Realm.class);
        mQuery = mock(RealmQuery.class);

        when(Realm.getDefaultInstance()).thenReturn(mRealm);

        mRealm = Realm.getDefaultInstance();

        when(mRealm.where(AirlineRealm.class)).thenReturn(mQuery);
        when(mQuery.equalTo(anyString(), anyString())).thenReturn(mQuery);

        when(mRealm.createObject(eq(AirlineRealm.class), eq(AIRLINE_CODE_1))).thenReturn(
                mock(AirlineRealm.class));
    }

    @Test
    public void testFindAll() {
        AirlineHelper.findAll(mRealm);

        verify(mRealm).where(AirlineRealm.class);
        verify(mQuery).findAllSorted("name", Sort.ASCENDING);
    }

    @Test
    public void testFindByCode() {
        String code = "Code1";

        AirlineHelper.findByCode(code, mRealm);

        verify(mRealm).where(AirlineRealm.class);
        verify(mQuery).equalTo("code", code);
        verify(mQuery).findFirst();
    }

    @Test
    public void testSaveAirline() {
        String code = AIRLINE_CODE_1;
        String name = "Airline1";
        String logo = "Logo";
        String website = "Website";
        String phone = "Phone";

        AirlineRealm airlineRealm = AirlineHelper.save(code, name, logo, phone, website, mRealm);

        verify(mRealm).where(AirlineRealm.class);
        verify(mQuery).equalTo("code", code);
        verify(mQuery).findFirst();

        verify(mRealm).createObject(AirlineRealm.class, code);

        verify(airlineRealm).setName(name);
        verify(airlineRealm).setLogo(logo);
        verify(airlineRealm).setWebsite(website);
        verify(airlineRealm).setPhone(phone);
    }

    @Test
    public void testSaveAirlineExiting() {
        String code = AIRLINE_CODE_2;
        String name = "Airline1";
        String logo = "Logo";
        String website = "Website";
        String phone = "Phone";

        when(mQuery.findFirst()).thenReturn(mock(AirlineRealm.class));

        AirlineRealm airlineRealm = AirlineHelper.save(code, name, logo, phone, website, mRealm);

        verify(mRealm).where(AirlineRealm.class);
        verify(mQuery).equalTo("code", code);
        verify(mQuery).findFirst();

        verify(mRealm, never()).createObject(AirlineRealm.class, code);

        verify(airlineRealm).setName(name);
        verify(airlineRealm).setLogo(logo);
        verify(airlineRealm).setWebsite(website);
        verify(airlineRealm).setPhone(phone);
    }

    @Test
    public void testUpdateAirline() {
        String code = AIRLINE_CODE_1;
        String name = "Airline1";
        String logo = "Logo";
        String website = "Website";
        String phone = "Phone";
        boolean favorite = true;

        when(mQuery.findFirst()).thenReturn(mock(AirlineRealm.class));

        AirlineRealm airlineRealm = AirlineHelper.update(code, name, logo, phone, website, favorite,
                mRealm);

        verify(mRealm).where(AirlineRealm.class);
        verify(mQuery).equalTo("code", code);
        verify(mQuery).findFirst();

        verify(airlineRealm).setName(name);
        verify(airlineRealm).setLogo(logo);
        verify(airlineRealm).setWebsite(website);
        verify(airlineRealm).setPhone(phone);
        verify(airlineRealm).setFavorite(favorite);
    }
}
