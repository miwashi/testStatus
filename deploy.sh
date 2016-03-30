gradle clean stage -Dorg.gradle.daemon=true
cd ./src/main/ansible/
ansible-playbook site.yml
cd ../../..