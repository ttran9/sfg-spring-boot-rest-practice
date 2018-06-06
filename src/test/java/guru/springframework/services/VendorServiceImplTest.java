package guru.springframework.services;

import guru.springframework.api.v1.mapper.VendorMapper;
import guru.springframework.api.v1.model.VendorDTO;
import guru.springframework.controllers.v1.VendorController;
import guru.springframework.domain.Vendor;
import guru.springframework.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VendorServiceImplTest {
    private final static String NAME = "Coca Cola Solutions";
    private final static String NAME_MODIFIED = "Coca Cola Solutions MODIFIED";
    private final static long ID = 1L;

    VendorService vendorService;

    @Mock
    VendorRepository vendorRepository;

    VendorMapper vendorMapper = VendorMapper.INSTANCE;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        vendorService = new VendorServiceImpl(vendorMapper, vendorRepository);
    }

    @Test
    public void getAllVendors() throws Exception {
        // given
        List<Vendor> vendors = Arrays.asList(new Vendor(), new Vendor());

        // when
        when(vendorRepository.findAll()).thenReturn(vendors);
        List<VendorDTO> vendorsList = vendorService.getAllVendors();

        // then
        assertEquals(vendors.size(), vendorsList.size());
    }

    @Test
    public void getVendorById() throws Exception {
        //given
        Vendor vendor = getVendor(NAME, ID);

        // when
        when(vendorRepository.findById(anyLong())).thenReturn(Optional.ofNullable(vendor));

        // then
        VendorDTO vendorDTO = vendorService.getVendorById(ID);
        assertEquals(vendor.getName(), vendorDTO.getName());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getVendorByNonExistingId() throws Exception {
        // when
        given(vendorRepository.findById(anyLong())).willReturn(Optional.empty());
        vendorService.getVendorById(ID);

        // then
        then(vendorRepository).should(times(1)).findById(anyLong());

    }

    @Test
    public void createNewVendor() throws Exception {
        // given
        VendorDTO vendorDTO = getVendorDTO(NAME, ID);
        Vendor createdVendor = getVendor(NAME, ID);

        // when
        when(vendorRepository.save(any(Vendor.class))).thenReturn(createdVendor);
        VendorDTO savedVendorDTO = vendorService.createNewVendor(vendorDTO);

        // then
        assertEquals(vendorDTO.getName(), savedVendorDTO.getName());
        assertEquals(vendorDTO.getVendorUrl(), savedVendorDTO.getVendorUrl());
    }

    @Test
    // test PUT request.
    public void updateVendor() throws Exception {
        // given
        VendorDTO vendorDTO = getVendorDTO(NAME, ID);
        Vendor savedVendor = getVendor(NAME, ID);

        // when
        when(vendorRepository.save(any(Vendor.class))).thenReturn(savedVendor);
        VendorDTO savedVendorDTO = vendorService.updateVendor(ID, vendorDTO);

        // then
        assertEquals(vendorDTO.getName(), savedVendorDTO.getName());
        assertEquals(vendorDTO.getVendorUrl(), savedVendorDTO.getVendorUrl());
    }

    @Test
    // test PATCH request
    public void patchVendor() throws Exception {
        // given
        VendorDTO vendorDTO = getVendorDTO(NAME_MODIFIED, ID);
        Vendor vendor = getVendor(NAME_MODIFIED, ID);

        given(vendorRepository.findById(anyLong())).willReturn(Optional.of(vendor));
        given(vendorRepository.save(any(Vendor.class))).willReturn(vendor);

        // when
        VendorDTO savedVendorDTO = vendorService.patchVendor(ID, vendorDTO);

        // then
        then(vendorRepository).should().save(any(Vendor.class));
        then(vendorRepository).should(times(1)).findById(anyLong());
        assertEquals(vendorDTO.getVendorUrl(), savedVendorDTO.getVendorUrl());
    }

    @Test
    public void deleteVendorById() throws Exception {
        // when
        vendorService.deleteVendorById(ID);

        // then
        verify(vendorRepository, times(1)).deleteById(anyLong());
        /**
         * jt's solution
         */
//        vendorRepository.deleteById(ID);
//        verify(vendorRepository, times(1)).deleteById(anyLong());
    }

    private Vendor getVendor(String name, Long id) {
        Vendor vendor = new Vendor();
        vendor.setName(name);
        vendor.setId(id);
        return vendor;
    }

    private VendorDTO getVendorDTO(String name, Long id) {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(name);
        vendorDTO.setVendorUrl(VendorController.VENDOR_BASE_URL + "/" + id);
        return vendorDTO;
    }

}
