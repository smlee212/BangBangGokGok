import React from "react";
import Slider from "react-slick";
import styled from "styled-components";
import DummyImg from "@/assets/common/DummyImg.png";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

interface IProps {
  data: ISliderData[];
}

export default function RankSlider({ data }: IProps) {
  const settings = {
    centerMode: true,
    dots: false,
    infinite: true,
    slidesToShow: 4,
    slidesToScroll: 4,
    arrows: false,
    autoplay: true,
    speed: 2000,
    autoplaySpeed: 2000,
    pauseOnHover: true,
    responsive: [
      {
        breakpoint: 1250,
        settings: {
          slidesToShow: 3,
          slidesToScroll: 3,
          infinite: true,
        },
      },
      {
        breakpoint: 900,
        settings: {
          slidesToShow: 2,
          slidesToScroll: 2,
          initialSlide: 2,
        },
      },
      {
        breakpoint: 640,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1,
        },
      },
    ],
  };
  return (
    <Container>
      <Title>{data[0].label}</Title>
      <Slider {...settings}>
        {data[0].themes.map((item, idx) => (
          <>
            <SliderItem key={idx}>
              <Rank>{item.themeId}</Rank>
              <img
                src={item.imgUrl}
                alt="img"
                style={{ width: "18rem", height: "23rem" }}
              />
            </SliderItem>
          </>
        ))}
      </Slider>
    </Container>
  );
}

const Container = styled.div`
  margin: 3rem auto;
  width: 90%;
`;

const Title = styled.div`
  margin: 3rem auto;
  font-size: 2.4rem;
  font-weight: bold;
  font-family: Pretendard;
  margin-top: 3rem;
`;

const SliderItem = styled.div`
  height: 100%;
  display: flex;
  justify-content: center;
  @media (max-width: 1200px) {
    width: 10rem;
  }
  img {
    margin-left: 1.2rem;
    cursor: pointer;
  }
`;

const Rank = styled.div`
  width: 7.5rem;
  font-family: Pretendard;
  font-size: 15rem;
  font-weight: bold;
  @media (max-width: 1300px) {
    font-size: 12rem;
  }
  @media (max-width: 1100) {
    font-size: 10rem;
  }
`;
