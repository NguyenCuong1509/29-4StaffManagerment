package com.updateStaffManagerment.project.config;

import org.mapstruct.MapperConfig; // Cấu hình chung cho mapper (Bài 5 - MapStruct)
import org.mapstruct.MappingConstants; // Hằng số component model của MapStruct (Bài 5 - MapStruct)

@MapperConfig(componentModel = MappingConstants.ComponentModel.SPRING) // Mapper sinh ra sẽ là Spring Bean (Bài 5 - MapStruct)
public interface CentralMapperConfig { // Config dùng chung cho toàn bộ mapper (Bài 5 - MapStruct)
}
