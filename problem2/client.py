# client.py
import socket
import os
import pickle
import time
import logging
import configparser

logging.basicConfig(filename='client.log', level=logging.INFO, format='%(asctime)s - %(message)s')

def get_host_port():
    # ConfigParser 객체를 생성합니다.
    config = configparser.ConfigParser()
    config.read('.client_config')

    # 설정 파일에서 호스트와 포트 읽어오기
    host = config.get('DEFAULT', 'Host')
    port = config.getint('DEFAULT', 'Port')

    return host, port

def client_program():
    host, port = get_host_port()

    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)  # socket 객체를 생성합니다.
    client_socket.connect((host, port))  # 서버에 연결을 요청합니다.
    logging.info('Connected to the server.')

    directory_to_watch = os.path.join(os.path.dirname(os.path.realpath(__file__)), 'dummy_client_folder')

    try:
        while True:
            client_files = {file: os.stat(os.path.join(directory_to_watch, file)).st_mtime for file in os.listdir(directory_to_watch)}  # 클라이언트의 파일들과 그들의 최종 수정 시간을 저장하는 해시 테이블
            client_socket.send(pickle.dumps(client_files))  # 해시 테이블을 직렬화하여 서버에 전송합니다.
            logging.info('Sent the file metadata to the server.')

            for file, mtime in client_files.items():
                with open(os.path.join(directory_to_watch, file), 'rb') as f:  # 파일을 열어서
                    client_socket.send(f.read())  # 데이터를 서버에 전송합니다.
                logging.info(f'Sent the file {file} to the server.')

            time.sleep(10)  # 10초 동안 대기합니다.
    finally:
        client_socket.close()  # 연결을 닫습니다.

if __name__ == '__main__':
    client_program()