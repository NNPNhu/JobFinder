
import { Alert, Button, Card, Col, Row } from "react-bootstrap";
import { useContext, useEffect, useState } from "react";
import Apis, { authApis, endpoints } from "../configs/Apis";
import MySpinner from "./layout/MySpinner";
import { MyDispatchContext, MyUserContext } from "../configs/Contexts";

const AllApplication = () => {
  const [allApplications, setAllApplications] = useState([]);
  const [loading, setLoading] = useState(false);
  // const [page, setPage] = useState(1);
  const [cvVisible, setCvVisible] = useState({}); // 🆕 Theo dõi trạng thái hiển thị ảnh CV
  const user = useContext(MyUserContext);

  const loadAllApplications = async () => {
    console.log(authApis);

    // if (page > 0) {
    try {
      setLoading(true);
      let url = `${endpoints['get_all_applications']}`;
      let res = await authApis().get(url); 

      setAllApplications(res.data);

      // if (res.data.length === 0)
      //   setPage(0);
      // else {
      //   if (page === 1)
      //     setApplications(res.data);
      //   else
      //     setApplications([...applications, ...res.data]);
      // }
    } catch (ex) {
      console.error(ex);
    } finally {
      setLoading(false);
    }
    // }
  };

  const toggleCV = (id) => {
    setCvVisible(prev => ({ ...prev, [id]: !prev[id] }));
  };

  const acceptApplication = async (id) => {
    try {
      await Apis.patch(`${endpoints.accept}${id}`);
      alert("Đã chấp nhận đơn ứng tuyển!");
      setAllApplications(allApplications.filter(app => app.id !== id));
    } catch (err) {
      console.error(err);
      alert("Lỗi khi chấp nhận đơn!");
    }
  };

  const rejectApplication = async (id) => {
    try {
      await Apis.patch(`${endpoints.reject}${id}`);
      alert("Đã từ chối đơn ứng tuyển!");
      setAllApplications(allApplications.filter(app => app.id !== id));
    } catch (err) {
      console.error(err);
      alert("Lỗi khi từ chối đơn!");
    }
  };

  useEffect(() => {
    loadAllApplications();
  }, []);

  // const loadMore = () => {
  //   if (!loading && page > 0)
  //     setPage(page + 1);
  // };

  return (
    <>
      {allApplications.length === 0 && <Alert variant="info" className="mt-1">Không có đơn ứng tuyển nào!</Alert>}

      <Row>
        {allApplications.map(p => (
          <Col className="p-1" key={p.id} md={10} xs={4}>
            <Card style={{ width: '80rem' }}>
              <Card.Body>
                <Card.Title>Ứng viên: {p.userId}</Card.Title>
                <Card.Text>Thư giới thiệu: {p.coverLetter}</Card.Text>

                <Button
                  variant="info"
                  className="mb-2"
                  onClick={() => toggleCV(p.id)}
                >
                  {cvVisible[p.id] ? "Ẩn CV" : "Xem CV"}
                </Button>

                {cvVisible[p.id] && (
                  <Card.Img
                    variant="top"
                    src={p.resumeLink}
                    alt="CV Ứng viên"
                    style={{ maxWidth: "500px", marginTop: "10px" }}
                  />
                )}

                {/* Trạng thái */}
                <Card.Text
                  className={p.status === 'REJECTED' ? 'text-danger' : 'text-info'}>
                  Trạng thái: {p.status}
                </Card.Text>


            
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>

      {loading && <MySpinner />}

      {/* {page > 0 && (
        <div className="text-center m-1">
          <Button variant="success" onClick={loadMore}>Xem thêm...</Button>
        </div>
      )} */}
    </>
  );
};

export default AllApplication;
