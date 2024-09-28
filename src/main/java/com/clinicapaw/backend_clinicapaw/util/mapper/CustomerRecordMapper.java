package com.clinicapaw.backend_clinicapaw.util.mapper;

import com.clinicapaw.backend_clinicapaw.persistence.model.CustomerRecord;
import com.clinicapaw.backend_clinicapaw.presentation.dto.CustomerRecordDTO;

public class CustomerRecordMapper {

    public static CustomerRecordDTO customerRecordDTOToCustomerRecord(CustomerRecordDTO customerRecord) {
        return CustomerRecordDTO.builder().build();
    }

    public static CustomerRecord customerRecordToCustomerRecordDTO(CustomerRecordDTO customerRecordDTO) {
        return CustomerRecord.builder().build();
    }
}
