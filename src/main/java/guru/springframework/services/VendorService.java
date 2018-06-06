package guru.springframework.services;

import guru.springframework.api.v1.model.VendorDTO;

import java.util.List;

public interface VendorService {
    List<VendorDTO> getAllVendors();
    VendorDTO createNewVendor(VendorDTO vendorDTO);
    VendorDTO getVendorById(Long id);
    VendorDTO updateVendor(Long id, VendorDTO vendorDTO);
    VendorDTO patchVendor(Long id, VendorDTO vendorDTO);
    void deleteVendorById(Long id);
}
