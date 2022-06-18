package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections
                    .stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections
                    .stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void add(final Line line, final Station upStation, final Station downStation, final int distance) {
        boolean isUpStationExisted = hasStation(upStation);
        boolean isDownStationExisted = hasStation(downStation);

        validateStationsToAdd(isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            updateUpStation(upStation, downStation, distance);
        }
        if (isDownStationExisted) {
            updateDownStation(upStation, downStation, distance);
        }

        sections.add(new Section(line, upStation, downStation, distance));
    }

    private boolean hasStation(final Station station) {
        return sections
                .stream()
                .anyMatch(section -> section.getUpStation() == station || section.getDownStation() == station);
    }

    private void validateStationsToAdd(final boolean isUpStationExisted, final boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
        if (!sections.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void updateUpStation(final Station upStation, final Station downStation, final int distance) {
        sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void updateDownStation(final Station upStation, final Station downStation, final int distance) {
        sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    public void removeStation(final Line line, final Station station) {
        validateSectionsToRemoveStation();

        Optional<Section> sectionToRemoveUpStation = getSectionByUpStation(station);
        Optional<Section> sectionToRemoveDownStation = getSectionByDownStation(station);

        if (sectionToRemoveUpStation.isPresent() && sectionToRemoveDownStation.isPresent()) {
            attachRemainingStations(line, sectionToRemoveUpStation.get(), sectionToRemoveDownStation.get());
        }

        sectionToRemoveUpStation.ifPresent(it -> sections.remove(it));
        sectionToRemoveDownStation.ifPresent(it -> sections.remove(it));
    }

    private void validateSectionsToRemoveStation() {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }
    }

    private Optional<Section> getSectionByUpStation(final Station upStation) {
        return sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst();
    }

    private Optional<Section> getSectionByDownStation(final Station downStation) {
        return sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst();
    }

    private void attachRemainingStations(final Line line,
                                         final Section sectionToRemoveUpStation,
                                         final Section sectionToRemoveDownStation) {
        final Station newUpStation = sectionToRemoveDownStation.getUpStation();
        final Station newDownStation = sectionToRemoveUpStation.getDownStation();
        int newDistance = sectionToRemoveUpStation.getDistance() + sectionToRemoveDownStation.getDistance();
        sections.add(new Section(line, newUpStation, newDownStation, newDistance));
    }
}
