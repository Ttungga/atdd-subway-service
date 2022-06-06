package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineAcceptanceTestMethod.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTestMethod.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.path.PathAcceptanceTestMethod.지하철_최단경로_조회_실패;
import static nextstep.subway.path.PathAcceptanceTestMethod.지하철_최단경로_조회_요청;
import static nextstep.subway.path.PathAcceptanceTestMethod.지하철_최단경로_조회됨;
import static nextstep.subway.station.StationAcceptanceTest.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        LineRequest 신분당선_Request = LineRequest.of("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
        LineRequest 이호선_Request = LineRequest.of("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10);
        LineRequest 삼호선_Request = LineRequest.of("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5);

        신분당선 = 지하철_노선_등록되어_있음(신분당선_Request).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(이호선_Request).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(삼호선_Request).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선.getId(), SectionRequest.of(교대역.getId(), 남부터미널역.getId(), 3));
    }

    @DisplayName("출발역에서 도착역까지 최단경로를 조회한다.")
    @Test
    void findShortPath01() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 양재역.getId());

        // then
        지하철_최단경로_조회됨(response, Arrays.asList(), Distance.from(1));
    }

    @DisplayName("출발역과 도착역이 같은 경우, 최단경로 조회가 실패한다.")
    @Test
    void exceptionFindShortPath01() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 교대역.getId());

        // then
        지하철_최단경로_조회_실패(response);
    }

    @DisplayName("존재하지 않은 출발역을 조회하는 경우, 최단경로 조회가 실패한다.")
    @Test
    void exceptionFindShortPath02() {
        // given
        StationResponse 수서역 = 지하철역_등록되어_있음("수서역").as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(수서역.getId(), 교대역.getId());

        // then
        지하철_최단경로_조회_실패(response);
    }

    @DisplayName("존재하지 않은 도착역을 조회하는 경우, 최단경로 조회가 실패한다.")
    @Test
    void exceptionFindShortPath03() {
        // given
        StationResponse 수서역 = 지하철역_등록되어_있음("수서역").as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 수서역.getId());

        // then
        지하철_최단경로_조회_실패(response);
    }
}
