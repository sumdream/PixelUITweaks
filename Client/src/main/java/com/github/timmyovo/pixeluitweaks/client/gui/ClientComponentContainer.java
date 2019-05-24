package com.github.timmyovo.pixeluitweaks.client.gui;

import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientComponentContainer {
    private ComponentContainer componentContainer;
    private ClientRenderMethod clientRenderMethod;

    public static ClientComponentContainer from(ComponentContainer componentContainer) {
        return ClientComponentContainer.builder()
                .componentContainer(componentContainer)
                .clientRenderMethod(ClientRenderMethod.fromRenderMethod(componentContainer.getRenderMethod()))
                .build();
    }
}
