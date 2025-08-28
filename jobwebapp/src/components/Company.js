import { Alert } from "react-bootstrap";
import { Button, Card, Col, Row } from "react-bootstrap";
import MySpinner from "./layout/MySpinner";
import { MyDispatchContext, MyUserContext } from "../configs/Contexts";
import { useContext, useEffect, useState } from "react";
import Apis, { authApis, endpoints } from "../configs/Apis";

const Companies = () => {
  const [companies, setCompanies] = useState([]);
  const [followedIds, setFollowedIds] = useState([]);
  const [loading, setLoading] = useState(false);
  const user = useContext(MyUserContext);

  const loadCompanies = async () => {
    try {
      setLoading(true);
      const res = await Apis.get(endpoints.companies);
          console.log("Endpoint:", endpoints['followed']);

      setCompanies(res.data);
    } catch (ex) {
      console.error("Lỗi khi tải công ty:", ex);
    } finally {
      setLoading(false);
    }
  };

const loadFollowedCompanies = async () => {
  try {
                // cookie.save('token', res.data.token);
    const res = await authApis().get(endpoints.followed);

    setFollowedIds(res.data);
  } catch (err) {
    console.error("Lỗi khi tải danh sách công ty đã theo dõi", err);
  }
};

const follow = async (id) => {
  try {
    await authApis().post(`${endpoints.follows}${id}`);
    alert("Đã theo dõi công ty!");
    setFollowedIds([...followedIds, id]);
  } catch (err) {
    console.error(err);
    alert("Lỗi khi theo dõi công ty!");
  }
};


  useEffect(() => {
    loadCompanies();
    loadFollowedCompanies();
  }, []);

  return (
    <>
      {companies.length === 0 && (
        <Alert variant="info" className="mt-1">
          Không có công ty nào!
        </Alert>
      )}

      <Row>
        {companies.map((p) => (
          <Col className="p-1" key={p.id} md={10} xs={4}>
            <Card style={{ width: "80rem" }}>
              <Card.Body>
                <Card.Title>Công ty: {p.name}</Card.Title>
                <Card.Text>Mô tả: {p.description}</Card.Text>
                <Card.Text>Địa chỉ: {p.address}</Card.Text>
                

                <Row className="mt-3 mb-3">
                  {p.image1 && (
                    <Col md={4} className="mb-2">
                      <Card.Img src={p.image1} alt="Ảnh công ty 1" style={{ height: "200px", objectFit: "cover" }} />
                    </Col>
                  )}
                  {p.image2 && (
                    <Col md={4} className="mb-2">
                      <Card.Img src={p.image2} alt="Ảnh công ty 2" style={{ height: "200px", objectFit: "cover" }} />
                    </Col>
                  )}
                  {p.image3 && (
                    <Col md={4} className="mb-2">
                      <Card.Img src={p.image3} alt="Ảnh công ty 3" style={{ height: "200px", objectFit: "cover" }} />
                    </Col>
                  )}
                </Row>


                {followedIds.includes(p.id) ? (
                  <Button className="m-1" variant="success" disabled>
                    Đã theo dõi
                  </Button>
                ) : (
                  <Button
                    className="m-1"
                    variant="primary"
                    onClick={() => follow(p.id)}
                  >
                    Theo dõi
                  </Button>
                )}
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>

      {loading && <MySpinner />}
    </>
  );
};

export default Companies;
