package ru.diasoft.micro.demo.dto;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
//import org.joda.time.DateTime;
//import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import ru.diasoft.micro.demo.repository.AutoModel;

@Component
@RequiredArgsConstructor
public class AutoModelMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
//        factory.getConverterFactory().registerConverter(new PassThroughConverter(DateTime.class));
//        factory.getConverterFactory().registerConverter(new PassThroughConverter(LocalDate.class));

        factory.classMap(AutoModelDto.class, AutoModel.class)
                .byDefault();
        factory.classMap(AutoModel.class, AutoModelDto.class)
                .byDefault();
    }

}
