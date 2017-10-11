package gov.samhsa.c2s.iexhubpixpdq.service;

import gov.samhsa.c2s.iexhubpixpdq.service.dto.FhirPatientDto;
import gov.samhsa.c2s.iexhubpixpdq.service.dto.PixPatientDto;
import gov.samhsa.c2s.iexhubpixpdq.service.exception.MrnNotFoundException;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.Identifier;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class PixPatientDtoConverter {

    public PixPatientDto fhirPatientDtoToPixPatientDto(FhirPatientDto fhirPatientDto) {
        PixPatientDto pixPatientDto = new PixPatientDto();
        pixPatientDto.setBirthTimeValue(getBirthDate(fhirPatientDto.getPatient().getBirthDate()));
        pixPatientDto.setPatientFirstName(fhirPatientDto.getPatient().getNameFirstRep().getGivenAsSingleString());
        pixPatientDto.setPatientLastName(fhirPatientDto.getPatient().getNameFirstRep().getFamily());
        pixPatientDto.setIdExtension(fhirPatientDto.getPatient().getIdentifier().stream()
                .filter(identifier -> identifier.getUse().equals(Identifier.IdentifierUse.OFFICIAL))
                .map(mrn -> mrn.getValue()).findAny().orElseThrow(MrnNotFoundException::new));
        String mrnSystem = fhirPatientDto.getPatient().getIdentifier().stream()
                .filter(identifier -> identifier.getUse().equals(Identifier.IdentifierUse.OFFICIAL))
                .map(mrn -> mrn.getSystem()).findAny().orElseThrow(MrnNotFoundException::new);

        //remove urn:oid:
        if (mrnSystem.toLowerCase().contains("urn:oid:")) {
            mrnSystem = org.apache.commons.lang.StringUtils.substringAfter(mrnSystem, "urn:oid:");
        }

        pixPatientDto.setIdRoot(mrnSystem);

        pixPatientDto.setAdministrativeGenderCode(getAdminGenderCode(fhirPatientDto.getPatient().getGender().name()));
        pixPatientDto.setTelecomValue(
                fhirPatientDto.getPatient().getTelecom().stream()
                        .filter(telecom -> telecom.getSystem().getDisplay().equalsIgnoreCase(ContactPoint.ContactPointSystem.PHONE.getDisplay()))
                        .map(ContactPoint::getValue).findFirst().orElse(""));

        pixPatientDto.setEmailValue(
                fhirPatientDto.getPatient().getTelecom().stream()
                        .filter(telecom -> telecom.getSystem().getDisplay().equalsIgnoreCase(ContactPoint.ContactPointSystem.EMAIL.getDisplay()))
                        .map(ContactPoint::getValue).findFirst().orElse(""));

        if (fhirPatientDto.getPatient().getAddress().isEmpty()) {
            pixPatientDto.setAddrStreetAddressLine1("");
        } else {
            pixPatientDto.setAddrStreetAddressLine1((fhirPatientDto.getPatient().getAddress().get(0).getLine() == null || fhirPatientDto.getPatient().getAddress().get(0).getLine().size() < 1) ? "" : fhirPatientDto.getPatient().getAddress().get(0)
                    .getLine().get(0).getValue());
            pixPatientDto.setAddrStreetAddressLine2((fhirPatientDto.getPatient().getAddress().get(0).getLine() == null || fhirPatientDto.getPatient().getAddress().get(0).getLine().size() < 2) ? "" : fhirPatientDto.getPatient().getAddress()
                    .get(0)
                    .getLine().get(1).getValue());
            pixPatientDto.setAddrCity((fhirPatientDto.getPatient().getAddress().get(0).getCity() == null) ? "" : fhirPatientDto.getPatient().getAddress().get(0).getCity().toString());
            pixPatientDto.setAddrState((fhirPatientDto.getPatient().getAddress().get(0).getState() == null) ? "" : fhirPatientDto.getPatient().getAddress().get(0).getState().toString());
            pixPatientDto.setAddrPostalCode((fhirPatientDto.getPatient().getAddress().get(0).getPostalCode() == null) ? "" : fhirPatientDto.getPatient().getAddress().get(0).getPostalCode().toString());
            pixPatientDto.setAddCountry((fhirPatientDto.getPatient().getAddress().get(0).getCountry() == null) ? "" : fhirPatientDto.getPatient().getAddress().get(0).getCountry().toString());
        }
        return pixPatientDto;
    }


    private String getAdminGenderCode(String genderName) {
        String genderCode = "U";
        if (genderName.equalsIgnoreCase(Enumerations.AdministrativeGender.MALE.name())) {
            genderCode = "M";
        } else if (genderName.equalsIgnoreCase(Enumerations.AdministrativeGender.FEMALE.name())) {
            genderCode = "F";
        } else if (genderName.equalsIgnoreCase(Enumerations.AdministrativeGender.OTHER.name())) {
            genderCode = "O";
        } else if (genderName.equalsIgnoreCase(Enumerations.AdministrativeGender.UNKNOWN.name())) {
            genderCode = "U";
        }
        return genderCode;
    }


    private String getBirthDate(Date utilDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(utilDate);
    }

}
