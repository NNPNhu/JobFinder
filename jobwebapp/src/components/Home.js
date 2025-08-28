import React, { useContext, useEffect, useState } from 'react';
import PlacesAutocomplete from 'react-places-autocomplete';
import { Link } from 'react-router-dom';
import { Alert, Button, Col, Container, Form, Row, Card } from 'react-bootstrap';
import { useLocation, useNavigate } from 'react-router-dom';
import { endpoints, authApis } from '../configs/Apis';
import Api from '../configs/Apis';
import JobCard from './JobCard';
import cookie from 'react-cookies';
import { MyDispatchContext, MyUserContext } from '../configs/Contexts';
import { useSearchParams } from 'react-router-dom';

const Home = () => {
    const user = useContext(MyUserContext);
    const [q, setSearchParams] = useSearchParams();

    const [jobs, setJobs] = useState([]);
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const pageSize = 10;

    const [searchParams, setSearchParamsState] = useState({
        kw: q.get('kw') || '',
        categoryId: q.get('categoryId') || '',
        salaryFrom: q.get('salaryFrom') || '',
        salaryTo: q.get('salaryTo') || '',
        location: q.get('location') || '',
        workingTime: q.get('workingTime') || ''
    });
    const fetchCategories = async () => {
        try {
            const response = await Api.get(endpoints['categories']); // Giả sử endpoint là "/api/categories"
            setCategories(response.data);
        } catch (error) {
            console.error('Error fetching categories:', error);
        }
    };

    const loadJobs = async () => {
        try {
            setLoading(true);
            let url = endpoints['jobpostings']; // Đảm bảo endpoints['jobpostings'] là "/jobs"
            // let kw = q.get('kw');
            // if (kw) {
            //     url = `${url}?kw=${kw}`; // Thêm query parameter
            // }

            // let res = await Api.get(url); // Gọi GET thay vì POST

            const params = { page: currentPage };

            if (searchParams.kw) params.kw = searchParams.kw;
            if (searchParams.categoryId) params.categoryId = searchParams.categoryId;
            if (searchParams.salaryFrom) params.salaryFrom = searchParams.salaryFrom;
            if (searchParams.salaryTo) params.salaryTo = searchParams.salaryTo;
            if (searchParams.location) params.location = searchParams.location;
            if (searchParams.workingTime) params.workingTime = searchParams.workingTime;

            const res = await Api.get(url, { params });
            console.log("[DEBUG] Phản hồi từ API /jobs:", res.data);

            if (!Array.isArray(res.data)) {
                console.error("Dữ liệu trả về không phải mảng:", res.data);
                setError("Dữ liệu không đúng định dạng.");
                return;
            }
            setJobs(res.data);
            const totalJobs = parseInt(res.headers['x-total-count'], 10) || 0;
            setTotalPages(Math.ceil(totalJobs / pageSize));

            // if (res.data.length === 0) {
            //     setPage(0);
            // } else {
            //     if (page === 1) {
            //         setJobs(res.data);
            //     } else {
            //         setJobs([...jobs, ...res.data]);
            //     }
            // }
            console.log('Avatar URL:', jobs.companyId.userAvatar);
        } catch (ex) {
            console.error("Lỗi khi tải công việc:", ex);
            setError("Không thể tải danh sách công việc.");
        } finally {
            setLoading(false);
        }
    };

    // useEffect(() => {
    //     if (page > 0)
    //         loadJobs();
    // }, [page, q]);

    useEffect(() => {
        fetchCategories();
    }, []);

    useEffect(() => {
        loadJobs();
    }, [currentPage, searchParams]);
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setSearchParamsState({ ...searchParams, [name]: value });
    };

    const handleSearch = (e) => {
        e.preventDefault();
        setCurrentPage(1); // Reset về trang 1 khi tìm kiếm
        setSearchParams(searchParams); // Cập nhật query params trong URL
    };

    // useEffect(() => {
    //     setPage(1);
    //     setJobs([]);
    // }, [q]);
    // Hàm xử lý chuyển trang
    const handlePageChange = (page) => {
        setCurrentPage(page);
    };
    return (
        <div>
            {/* <h2>Trang chủ</h2> 
            {user ? (
                <p>Xin chào, {user.username}!</p>
            ) : (
                <p>Vui lòng đăng nhập.</p>
            )} */}
            <div className="container mx-auto p-4">
            <Form onSubmit={handleSearch}>
                <Row className="mb-4">
                    <Col md={3}>
                        <Form.Control
                            type="text"
                            name="kw"
                            value={searchParams.kw}
                            onChange={handleInputChange}
                            placeholder="Tìm kiếm"
                        />
                    </Col>
                    <Col md={2}>
                        <Form.Control as="select" name="categoryId" value={searchParams.categoryId} onChange={handleInputChange}>
                            <option value="">Tất cả ngành nghề</option>
                            {categories.map((category) => (
                                <option key={category.id} value={category.id}>
                                    {category.name}
                                </option>
                            ))}
                        </Form.Control>
                    </Col>
                    <Col md={2}>
                        <Form.Control as="select" name="workingTime" value={searchParams.workingTime} onChange={handleInputChange}>
                            <option value="">Tất cả thời gian</option>
                            <option value="Toàn thời gian">Toàn thời gian</option>
                            <option value="Bán thời gian">Bán thời gian</option>
                            <option value="Cuối tuần">Cuối tuần</option>
                            <option value="Remote">Remote</option>
                        </Form.Control>
                    </Col>
                    <Col md={2}>
                        <Form.Control
                            type="number"
                            name="salaryFrom"
                            value={searchParams.salaryFrom}
                            onChange={handleInputChange}
                            placeholder="Mức lương tối thiểu"
                        />
                    </Col>
                    <Col md={2}>
                        <Form.Control
                            type="number"
                            name="salaryTo"
                            value={searchParams.salaryTo}
                            onChange={handleInputChange}
                            placeholder="Mức lương tối đa"
                        />
                    </Col>
                    <Col md={2}>
                        <Form.Control
                            type="text"
                            name="location"
                            value={searchParams.location}
                            onChange={handleInputChange}
                            placeholder="Địa chỉ"
                        />
                    </Col>
                    <Col md={1}>
                        <Button type="submit" variant="primary">Tìm kiếm</Button>
                    </Col>
                </Row>
            </Form>
                <h2 className="text-2xl font-bold mb-4">Danh sách công việc</h2>
                {jobs.length === 0 && <Alert variant="info" className="m-2">Không có sản phẩm nào!</Alert>}

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    {jobs.map((job) => (
                        <JobCard key={job.id} job={job} />
                    ))}
                </div>
            </div>
        </div>
    );
}
export default Home;