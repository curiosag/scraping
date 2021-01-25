package google.maps.dao;

import google.maps.Const;
import google.maps.searchapi.PlaceSearchResultItem;
import google.maps.Point;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static persistence.Common.createConnection;

public class PlacesDao {
    private final Connection connection = createConnection(Const.connectionUrl, true);

    public List<PlaceSearchResultItem> getPlaces() {
        String query = "select place_id, name, plus_compound_code, global_code, vicinity, ST_AsText(geom) as geom\n" +
                "from places_scraped";

        List<PlaceSearchResultItem> result = new ArrayList<>();
        try {
            ResultSet r = connection.createStatement().executeQuery(query);
            while (r.next()) {
                Point p = fromPointGeom(r.getString("geom"));
                result.add(new PlaceSearchResultItem(r.getString("place_id"), p.lat, p.lon, r.getString("name"),
                        r.getString("plus_compound_code"), r.getString("global_code"), r.getString("vicinity")));
            }
            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Point fromPointGeom(String geom) {
        String[] coord = geom.replace("POINT(", "").replace(")", "").split(" ");
        return new Point(Double.parseDouble(coord[0]), Double.parseDouble(coord[1]));
    }

}
