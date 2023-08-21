package com.sparta.lafesta.festival.controller;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.common.security.UserDetailsImpl;
import com.sparta.lafesta.festival.dto.FestivalRequestDto;
import com.sparta.lafesta.festival.dto.FestivalResponseDto;
import com.sparta.lafesta.festival.service.FestivalServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "페스티벌 관련 API", description = "페스티벌 관련 API 입니다.")
public class FestivalController {
    private final FestivalServiceImpl festivalService;

    @PostMapping("/festivals")
    @Operation(summary = "페스티벌 등록", description = "페스티벌을 생성합니다. Dto를 통해 정보를 받아와 festival을 생성할 때 해당 정보를 저장합니다.")
    public ResponseEntity<ApiResponseDto> createFestival(
            @Parameter(description = "festival을 생성할 때 필요한 정보") @RequestBody FestivalRequestDto requestDto,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        FestivalResponseDto result = festivalService.createFestival(requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.CREATED.value(), result.getTitle()+"를 추가했습니다."));
    }

    @GetMapping("/festivals")
    @Operation(summary = "페스티벌 전체 조회", description = "페스티벌을 전체 조회합니다.")
    public ResponseEntity<List<FestivalResponseDto>> selectFestivals() {
        List<FestivalResponseDto> results = festivalService.selectFestivals();
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/festivals/{festivalId}")
    @Operation(summary = "페스티벌 상세 조회", description = "@PathVariable을 통해 festivalId 받아와, 해당 festival을 상세 조회합니다.")
    public ResponseEntity<FestivalResponseDto> selectFestival(
            @Parameter(name = "festivalId", description = "선택한 festival의 id", in = ParameterIn.PATH) @PathVariable Long festivalId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        FestivalResponseDto result = festivalService.selectFestival(festivalId, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/festivals/{festivalId}")
    @Operation(summary = "페스티벌 내용 수정", description = "@PathVariable을 통해 festival Id를 받아와, 해당 페스티벌의 내용을 수정합니다. Dto를 통해 정보를 가져옵니다.")
    public ResponseEntity<FestivalResponseDto> modifyFestival(
            @Parameter(name = "festivalId", description = "수정할 festival의 id", in = ParameterIn.PATH) @PathVariable Long festivalId,
            @Parameter(description = "festival을 수정할 때 필요한 정보") @RequestBody FestivalRequestDto requestDto,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        FestivalResponseDto result = festivalService.modifyFestival(festivalId, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/festivals/{festivalId}")
    @Operation(summary = "페스티벌 삭제", description = "@PathVariable을 통해 festivalId를 받아와, 해당 페스티벌을 삭제합니다.")
    public ResponseEntity<ApiResponseDto> deleteFestival(
            @Parameter(name = "festivalId", description = "삭제할 페스티벌의 id", in = ParameterIn.PATH) @PathVariable Long festivalId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        festivalService.deleteFestival(festivalId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "페스티벌 삭제 완료"));
    }
}