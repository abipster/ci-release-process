# ci-release-process

Hi guys!

I've setted up an environment to develop the jenkins pipeline for the CoreHub Release Process, you can find the projects in my github space using as explained bellow. The existing structure allows to test everything just by running the provided vagrant environment.

- Pipeline project:

    - ci-release-process (https://github.com/abipster/ci-release-process)

- Vagrant:

    - vagrant-ci-demo (https://github.com/abipster/vagrant-ci-demo)

- Demo projects (used to mock the real projects):

    - demo-component (https://github.com/abipster/demo-component)

    - demo-component-test (https://github.com/abipster/demo-component-test)

    - demo-sanity-checks (https://github.com/abipster/demo-sanity-checks)

    - demo-integration-tests (https://github.com/abipster/demo-integration-tests)


The vagrant environment installs both jenkins and artifactory, the jenkins installation already has all the projects configured and ready to run (hopefully :p).

In the ci-release-process project you can find the file "src/main/resources/POC-release-pipeline.groovy" which has the current version of the pipeline and the folder img has whiteboard pictures of the intended solution.

The demo projects might need tweaks to work, but they are intended to be as simple as possible to mock the real projects.

Feel free to help developing this and to share it with anyone else. I'm here to answer any doubts.

Regards,

Diogo
