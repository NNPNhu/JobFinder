import React, { useEffect, useState } from "react";
import { Line } from "react-chartjs-2";
import { Form, Button } from "react-bootstrap";
import Apis, { endpoints } from "../configs/Apis";
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
} from "chart.js";

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

const StatsChart = () => {
    const [data, setData] = useState([]);
    const [time, setTime] = useState("month"); // month | quarter
    const [year, setYear] = useState(new Date().getFullYear());

    const loadStats = async () => {
        try {
            const res = await Apis.get(endpoints.stats, {
                params: {
                    time: time,
                    year: year
                }
            });
            setData(res.data);
        } catch (err) {
            console.error("❌ Lỗi khi tải thống kê:", err);
        }
    };

    useEffect(() => {
        loadStats();
    }, []);

    const chartData = {
        labels: data.map(d => d.time),
        datasets: [
            {
                label: "Số Job",
                data: data.map(d => d.jobCount),
                borderColor: "blue",
                fill: false
            },
            {
                label: "Số Candidate",
                data: data.map(d => d.candidateCount),
                borderColor: "green",
                fill: false
            },
            {
                label: "Số Employer",
                data: data.map(d => d.employerCount),
                borderColor: "red",
                fill: false
            }
        ]
    };

    const chartTitle = time === "month"
        ? `Thống kê theo tháng - ${year}`
        : `Thống kê theo quý - ${year}`;

    const options = {
        responsive: true,
        plugins: {
            title: {
                display: true,
                text: chartTitle
            }
        }
    };


    return (
        <div>
            <h2>📊 Biểu đồ thống kê</h2>
            <Form className="mb-3">
                <Form.Group>
                    <Form.Label>Thời gian</Form.Label>
                    <Form.Select value={time} onChange={(e) => setTime(e.target.value)}>
                        <option value="month">Tháng</option>
                        <option value="quarter">Quý</option>
                    </Form.Select>
                </Form.Group>
                <Form.Group className="mt-2">
                    <Form.Label>Năm</Form.Label>
                    <Form.Control type="number" value={year} onChange={(e) => setYear(e.target.value)} />
                </Form.Group>
                <Button className="mt-3" onClick={loadStats}>Lấy thống kê</Button>
            </Form>

            <Line data={chartData} options={options} />
        </div>
    );
};

export default StatsChart;
