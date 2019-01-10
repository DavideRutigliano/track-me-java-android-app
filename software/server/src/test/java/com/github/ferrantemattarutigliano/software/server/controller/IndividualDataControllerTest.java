package com.github.ferrantemattarutigliano.software.server.controller;

import com.github.ferrantemattarutigliano.software.server.constant.Message;
import com.github.ferrantemattarutigliano.software.server.model.dto.HealthDataDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.PositionDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.HealthData;
import com.github.ferrantemattarutigliano.software.server.model.entity.Position;
import com.github.ferrantemattarutigliano.software.server.service.IndividualDataService;
import com.github.ferrantemattarutigliano.software.server.service.RequestService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.parameters.P;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;

public class IndividualDataControllerTest {
    @InjectMocks
    IndividualDataController individualDataController;

    @Mock
    IndividualDataService mockIndividualDataService;

    @Mock
    RequestService mockRequestService;


    @Before
    public void initTest() {
        MockitoAnnotations.initMocks(this);
    }


    private HealthDataDTO createMockHealthDataDTO(Long Id, String name) {
        HealthDataDTO HDTO = new HealthDataDTO();
        HDTO.setId(Id);
        HDTO.setName(name);
        HDTO.setValue("100");
        HDTO.setDate(new Date(1));
        HDTO.setTime(new Time(1));
        //check test of getters
        Long a = HDTO.getId();
        String b = HDTO.getName();
        String c = HDTO.getValue();
        java.util.Date d = HDTO.getDate();
        Time e = HDTO.getTime();
        return HDTO;

    }

    public HealthData convertHealthDataDTO(HealthDataDTO HDTO) {
        ModelMapper modelMapper = new ModelMapper();
        HealthData H = modelMapper.map(HDTO, HealthData.class);
        return H;
    }

    @Test
    public void insertDateTest() {

        //create a collection of healthDataDTOs

        Collection<HealthDataDTO> healthDataDTOS = new ArrayList<>();
        Long x = 1L;
        int i = 0;
        while (i < 10) {
            HealthDataDTO H = createMockHealthDataDTO(0L + x, "parametro" + i);
            healthDataDTOS.add(H);
            i++;
        }

        //convert it into a collection of HealthDatas

        Collection<HealthData> healthDatas = new ArrayList<>();
        for (HealthDataDTO dtos : healthDataDTOS) {
            HealthData hdata = convertHealthDataDTO(dtos);
            healthDatas.add(hdata);
        }

        /* TEST STARTS HERE*/

        Mockito.when(mockIndividualDataService.insertData(healthDatas))
                .thenReturn(Message.INSERT_DATA_SUCCESS.toString());

        String result = individualDataController.insertData(healthDatas);

        Assert.assertEquals(Message.INSERT_DATA_SUCCESS.toString(), result);

    }

    @Test
    public void insertPositionTest() {

        //create mock positionDto

        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setLongitude(50.0);
        positionDTO.setLatitude(10.0);
        positionDTO.setDate(new Date(1));
        positionDTO.setTime(new Time(1));

        //convert into mock Positio

        ModelMapper modelMapper = new ModelMapper();
        Position position = modelMapper.map(positionDTO, Position.class);

        /* TEST STARTS HERE */

        individualDataController.insertPosition(position);

    }

}