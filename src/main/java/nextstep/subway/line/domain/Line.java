package nextstep.subway.line.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

//    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
//    private List<Section> sections = new ArrayList<>();
    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
//        sections.add(new Section(this, upStation, downStation, distance));
        sections.add(this, upStation, downStation, distance);
    }

    public List<Station> getStations() {
//        if (sections.isEmpty()) {
//            return Arrays.asList();
//        }
//
//        List<Station> stations = new ArrayList<>();
//        Station downStation = findUpStation();
//        stations.add(downStation);
//
//        while (downStation != null) {
//            Station finalDownStation = downStation;
//            Optional<Section> nextLineStation = sections
//                    .stream()
//                    .filter(it -> it.getUpStation() == finalDownStation)
//                    .findFirst();
//            if (!nextLineStation.isPresent()) {
//                break;
//            }
//            downStation = nextLineStation.get().getDownStation();
//            stations.add(downStation);
//        }
//
//        return stations;
        return sections.getStations();
    }

//    private Station findUpStation() {
//        Station downStation = sections.get(0).getUpStation();
//        while (downStation != null) {
//            Station finalDownStation = downStation;
//            Optional<Section> nextLineStation = sections.stream()
//                    .filter(it -> it.getDownStation() == finalDownStation)
//                    .findFirst();
//            if (!nextLineStation.isPresent()) {
//                break;
//            }
//            downStation = nextLineStation.get().getUpStation();
//        }
//
//        return downStation;
//    }

    public void update(final Line line) {
        name = line.getName();
        color = line.getColor();
    }

    public void addSection(final Station upStation, final Station downStation, final int distance) {
//        List<Station> stations = getStations();
//        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
//        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);
//
//        if (isUpStationExisted && isDownStationExisted) {
//            throw new RuntimeException("이미 등록된 구간 입니다.");
//        }
//
//        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
//                stations.stream().noneMatch(it -> it == downStation)) {
//            throw new RuntimeException("등록할 수 없는 구간 입니다.");
//        }
//
//        if (stations.isEmpty()) {
//            sections.add(new Section(this, upStation, downStation, distance));
//            return;
//        }
//
//        if (isUpStationExisted) {
//            sections.stream()
//                    .filter(it -> it.getUpStation() == upStation)
//                    .findFirst()
//                    .ifPresent(it -> it.updateUpStation(downStation, distance));
//
//            sections.add(new Section(this, upStation, downStation, distance));
//        } else if (isDownStationExisted) {
//            sections.stream()
//                    .filter(it -> it.getDownStation() == downStation)
//                    .findFirst()
//                    .ifPresent(it -> it.updateDownStation(upStation, distance));
//
//            sections.add(new Section(this, upStation, downStation, distance));
//        } else {
//            throw new RuntimeException();
//        }
        sections.add(this, upStation, downStation, distance);
    }

    public void removeStation(final Station station) {
//        if (sections.size() <= 1) {
//            throw new RuntimeException();
//        }
//
//        Optional<Section> upLineStation = sections.stream()
//                .filter(it -> it.getUpStation() == station)
//                .findFirst();
//        Optional<Section> downLineStation = sections.stream()
//                .filter(it -> it.getDownStation() == station)
//                .findFirst();
//
//        if (upLineStation.isPresent() && downLineStation.isPresent()) {
//            Station newUpStation = downLineStation.get().getUpStation();
//            Station newDownStation = upLineStation.get().getDownStation();
//            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
//            sections.add(new Section(this, newUpStation, newDownStation, newDistance));
//        }
//
//        upLineStation.ifPresent(it -> sections.remove(it));
//        downLineStation.ifPresent(it -> sections.remove(it));
        sections.removeStation(this, station);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
//        return sections;
        return null;
    }
}
