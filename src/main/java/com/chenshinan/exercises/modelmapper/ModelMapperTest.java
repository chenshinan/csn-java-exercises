package com.chenshinan.exercises.modelmapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;

/**
 * @author shinan.chen
 * @since 2019/4/15
 */
public class ModelMapperTest {
    public static void main(String[] args) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ADTO adto = new ADTO();
        adto.setDefaultValue("a");
        BDTO bdto = modelMapper.map(adto, BDTO.class);
        System.out.println(bdto);
    }
}
