package gov.samhsa.c2s.iexhubpixpdq.web;

import gov.samhsa.c2s.iexhubpixpdq.config.IexhubPixPdqProperties;
import gov.samhsa.c2s.iexhubpixpdq.service.PixOperationService;
import gov.samhsa.c2s.iexhubpixpdq.service.dto.FhirPatientDto;
import gov.samhsa.c2s.iexhubpixpdq.service.dto.PatientIdentifierDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PixOperationController {

    private final PixOperationService pixOperationService;

    @Autowired
    public PixOperationController(PixOperationService pixOperationService) {
        this.pixOperationService = pixOperationService;
    }

    @GetMapping("/patients/{patientId}/mrn-oid/{patientMrnOid}/enterprise-id")
    @ResponseStatus(HttpStatus.OK)
    public PatientIdentifierDto getPatientEid(@PathVariable String patientId,
                                             @PathVariable String patientMrnOid) {
        return pixOperationService.queryForEnterpriseId(patientId, patientMrnOid);
    }

    @GetMapping("/Patient")
    @ResponseStatus(HttpStatus.OK)
    public String getFhirPatient(@RequestParam String identifier) {
        return pixOperationService.searchPatientByMrn(identifier);
    }

    @PostMapping(value = "/Patient", consumes = IexhubPixPdqProperties.Fhir.MediaType.APPLICATION_FHIR_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void addPatient(@RequestBody FhirPatientDto fhirPatientDto) {
        pixOperationService.registerPatient(fhirPatientDto);
    }

    @PutMapping(value = "/Patient", consumes = IexhubPixPdqProperties.Fhir.MediaType.APPLICATION_FHIR_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updatePatient(@RequestBody FhirPatientDto fhirPatientDto, @RequestParam String identifier) {
        pixOperationService.editPatient(fhirPatientDto);
    }
}
