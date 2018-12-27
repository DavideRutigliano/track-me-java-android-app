package com.github.ferrantemattarutigliano.software.client.view.thirdparty;

import com.github.ferrantemattarutigliano.software.client.model.ThirdPartyDTO;

public interface ThirdPartyHomeView {
    void notifyUser();
    void onProfileFetch(ThirdPartyDTO thirdPartyDTO);
}
